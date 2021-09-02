package casehub.client.api.aml

import java.time.Instant
import org.http4s._
import org.http4s.circe.jsonEncoderOf

sealed trait AmlRequestBody

object AmlRequestBody extends AmlRequestBodyInstances {
  final case class Subscribe(
    externalId: String,
    expireAt: Instant,
    entities: List[AmlEntity],
    force: Boolean
  ) extends AmlRequestBody
}

sealed abstract class AmlRequestBodyInstances {
  import io.circe._
  import io.circe.generic.extras._
  implicit val config: Configuration = Configuration.default.withDiscriminator("_type")
  implicit lazy val enc: Encoder[AmlRequestBody] = {
    import io.circe.generic.auto._
    io.circe.generic.extras.semiauto.deriveConfiguredEncoder[AmlRequestBody]
  }
  implicit final def eenc[F[_]]: EntityEncoder[F, AmlRequestBody] = jsonEncoderOf[F, AmlRequestBody]
}
