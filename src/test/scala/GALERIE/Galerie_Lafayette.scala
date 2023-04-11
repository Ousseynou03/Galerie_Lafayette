package GALERIE


import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


import scala.language.postfixOps

class Galerie_Lafayette  extends  Simulation{

  private val host: String = System.getProperty("urlCible", "https://ws-oms-pp.galerieslafayette.com")
  private val VersionAppli: String = System.getProperty("VersionApp", "Vxx.xx.xx")
  private val TpsMonteEnCharge: Int = System.getProperty("tpsMonte", "3").toInt
  private val TpsPalier: Int = System.getProperty("tpsPalier", (2 * TpsMonteEnCharge).toString).toInt
  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt
  private val DureeMax: Int = System.getProperty("dureeMax", "1").toInt + 5 * (TpsMonteEnCharge + TpsPalier)

  private val LeCoeff: Int = System.getProperty("coeff", "10").toInt
  private val  nbVu : Int =  LeCoeff * 1


  val httpProtocol =   http
    .baseUrl(host)
    .acceptHeader("application/json")
    .header("Content-Type", "application/json")
    .header("X-GGL-CONTEXT-TENANT", "gl")
    .header("X-GGL-CONTEXT-CHANNEL", "ecom")
    .header("X-GGL-CONTEXT-SUBTENANT", "3050")


  before {

    println("----------------------------------------------" )
    println("host :"+ host   )
    println("VersionAppli :"+ VersionAppli   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("----------------------------------------------" )
  }

  after  {
    println("----------------------------------------------" )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("VersionAppli :"+ VersionAppli   )
    println("host :"+ host   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("DureeMax : " + DureeMax )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("----------------------------------------------" )
    println(" " )
  }


  val ecomExpress = scenario("Ecom Express").exec(ObjectEcomExpressSplit.scnEcomExpress)
  val livraisonExpress = scenario("Livraison Express ").exec(ObjectLivraisonExpress.scnLivraisonExpress)
  val rmkpLivraison = scenario("RMKP LIV").exec(ObjectRMKPLivraison.scnRmkpLivraison)
  val standardSplit = scenario("Standard Liv").exec(ObjectStandardSplit.scnStandardSplit)


  setUp(
    ecomExpress.inject(rampUsers(nbVu * 10) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    livraisonExpress.inject(rampUsers(nbVu * 10) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    rmkpLivraison.inject(rampUsers(nbVu * 10) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    standardSplit.inject(rampUsers(nbVu * 10) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes))
  ).protocols(httpProtocol)
    .maxDuration( DureeMax minutes)

}
