package casehub.client.api.aml

import casehub.client.api.base.ApiParams
import cats.data.Kleisli
import org.http4s.client.Client
import org.http4s._
import casehub.client.auth.TokenManager
import cats.effect._
import cats.implicits._
import org.http4s.headers.Authorization

object Aml {
  def command[F[_]: Sync](reqB: AmlRequestBody): Kleisli[F, (ApiParams, Client[F], TokenManager[F]), Unit] =
    Kleisli { case (apiParams, client, tm) =>
      tm.getToken.flatMap { resp =>
        val rawToken: String = resp.accessToken
        val req = Request[F](
          method = Method.POST,
          uri = apiParams.baseUri,
          headers = Headers(Authorization(Credentials.Token(AuthScheme.Bearer, rawToken)))
        ).withEntity(reqB)

        client.run(req).use { apiResp =>
          if (apiResp.status.isSuccess) {
            Sync[F].unit
          } else {
            Sync[F].raiseError(new Exception(s"failed to perform $req with response $apiResp"))
          }
        }
      }
    }
}
