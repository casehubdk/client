package casehub.client

import munit.CatsEffectSuite
import org.http4s.client.jdkhttpclient.JdkHttpClient
import cats.effect._
import casehub.client.auth._
import org.http4s.implicits._
import casehub.client.api.aml._
import casehub.client.api.base.ApiParams
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.LocalDate

class ApiIntegrationTest extends CatsEffectSuite {
  val client = ResourceSuiteLocalFixture(
    "http-client",
    JdkHttpClient.simple[IO]
  )

  override def munitFixtures: List[Fixture[_]] = client :: super.munitFixtures.toList

  val tdD = Deferred.unsafe[IO, TokenManager[IO]]

  test(s"should get a token") {
    val authParams = AuthParams(
      audience = "Gateway",
      grantType = "client_credentials",
      clientId = "hS505tEQXHgQzEXaKmV8RFjaUwnDxkQZ",
      clientSecret = System.getenv("AUTH0_CLIENT_SECRET")
    )
    val tdF = TokenManager[IO](uri"https://casehub.eu.auth0.com/oauth/token", authParams).run(client())
    tdF.flatMap { td =>
      tdD.complete(td) >> td.getToken.flatTap(x => IO.println(x))
    }
  }

  test(s"should send an item to casehub") {
    val apiParams = ApiParams(uri"https://casehub.dk/api/aml")
    tdD.get.flatMap { td =>
      Aml
        .command[IO](
          AmlRequestBody.Subscribe(
            externalId = "test",
            expireAt = Instant.now().plus(700, ChronoUnit.DAYS),
            entities = List[AmlEntity](
              AmlEntity.InputPerson(
                name = "Osama bin Larden",
                birthday = LocalDate.of(1957, 3, 10)
              )
            ),
            force = true
          )
        )
        .run((apiParams, client(), td))
    }
  }
}
