package GALERIE

import GALERIE.ObjectEcomExpressSplit.TpsPause

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.language.postfixOps

object ObjectStandardSplit {

  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt

  val headers = Map(
    "Content-Type" -> "application/json",
    "X-GGL-CONTEXT-TENANT" -> "gl",
    "X-GGL-CONTEXT-RESERVATION" -> "false",
    "X-GGL-CONTEXT-GENERATION-ID" -> "false",
    "Authorization" -> "Basic c3BsaXQ6c3BsaXQ="
  )

  val scnStandardSplit = scenario("Mode Livraison Standard")
    .exec(http("Split Standard")
      .post("/order-split-api/api/v1/split")
      .headers(headers)
      .asJson
      .body(StringBody(
        """{
          |    "typology": "ECOM",
          |    "commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |    "shop_code": "0000",
          |    "products_details": [
          |        {
          |            "uga": "24529917",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        },
          |        {
          |            "uga": "22460065",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        },
          |        {
          |            "uga": "44038490",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        }
          |    ]
          |}""".stripMargin)).asJson
      .check(status.is(200))
    )
    .pause(TpsPause.second)


}