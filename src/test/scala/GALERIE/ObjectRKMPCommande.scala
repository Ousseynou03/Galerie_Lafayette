package GALERIE

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


import scala.language.postfixOps

object ObjectRKMPCommande {



  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt



  val headersECOM = Map(
    "X-GGL-CONTEXT-TENANT" -> "gl",
    "X-GGL-CONTEXT-CHANNEL" -> "ecom",
    "X-GGL-CONTEXT-SUBTENANT" -> "3050",
    "Content-Type" -> "application/json",
  )

  val scnRMKPCommande = scenario("OMS API Split")
    .exec(http("RMKP Split")
      .post("/order-split-api/api/v1/split")
      .headers(headersECOM)
      .asJson
      .body(StringBody(
        """{
          |  "typology": "RMKP",
          |  "products_details": [
          |    {
          |      "uga": "24529917",
          |      "quantity": 5,
          |      "delivery_mode": "STANDARD_DELIVERY"
          |    },
          |    {
          |      "uga": "22460065",
          |      "quantity": 5,
          |      "delivery_mode": "STANDARD_DELIVERY"
          |    },
          |    {
          |      "uga": "44038490",
          |      "quantity": 5,
          |      "delivery_mode": "STANDARD_DELIVERY"
          |    },
          |    {
          |      "uga": "14264659",
          |      "quantity": 56,
          |      "delivery_mode": "STANDARD_DELIVERY"
          |    }
          |  ]
          |}""".stripMargin)).asJson
      .check(status.is(200))
    )
    .pause(TpsPause.second)

}
