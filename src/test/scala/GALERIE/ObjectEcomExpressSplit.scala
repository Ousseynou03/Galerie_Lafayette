package GALERIE

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.language.postfixOps

object ObjectEcomExpressSplit {


  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt

  val headersECOM = Map(
    "Content-Type" -> "application/json",
    "X-GGL-CONTEXT-TENANT" -> "gl",
    "X-GGL-CONTEXT-RESERVATION" -> "false",
    "X-GGL-CONTEXT-GENERATION-ID" -> "false",
    "Authorization" -> "Basic c3BsaXQ6c3BsaXQ=")

  val scnEcomExpress = scenario("ECOM Express Split")
    .exec(http("ECOM Express Split")
      .post("/order-split-api/api/v1/split")
      .headers(headersECOM)
      .asJson
      .body(StringBody(
        """{
          |  "typology": "ECOM",
          |  "commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |  "shop_code": "0000",
          |  "products_details": [
          |    {
          |      "uga": "24529917",
          |      "quantity": 5,
          |      "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |    },
          |    {
          |      "uga": "29167445",
          |      "quantity": 5,
          |      "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |    },
          |    {
          |      "uga": "44038490",
          |      "quantity": 5,
          |      "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |    }
          |  ]
          |}""".stripMargin)).asJson
      .check(status.is(200))
    )
    .pause(TpsPause.second)

}
