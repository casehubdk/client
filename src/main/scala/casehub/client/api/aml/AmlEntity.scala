package casehub.client.api.aml

import java.time.LocalDate
import org.http4s.circe.jsonEncoderOf
import org.http4s.EntityEncoder

sealed trait AmlEntity

object AmlEntity extends AmlEntityInstances {
  final case class InputPerson(
    name: String,
    birthday: LocalDate
  ) extends AmlEntity

  final case class InputCompany(
    cvr: String
  ) extends AmlEntity
}

sealed abstract class AmlEntityInstances {
  import io.circe.generic.extras.Configuration
  implicit val config: Configuration = Configuration.default.withDiscriminator("_type")
  implicit lazy val enc = io.circe.generic.extras.semiauto.deriveConfiguredEncoder[AmlEntity]
  implicit final def eenc[F[_]]: EntityEncoder[F, AmlEntity] = jsonEncoderOf[F, AmlEntity]
}
