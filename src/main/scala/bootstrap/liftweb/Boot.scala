package bootstrap.liftweb

import java.util.Locale

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import provider.HTTPRequest
import sitemap._
import Loc._
import mapper._

import code.model._
import code.lib.QuestionnaireServices

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Questionnaire, Question, Choice, Answer)
    
    if(Questionnaire.findAll isEmpty) {
      Questionnaire.createDefaultQuestionnaire
    }

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("HOME") / "index" submenus(
        Menu.i("GET_QUESTIONNAIRE") / "static" / "get",
        Menu.i("VERIFY_RESULTS") / "static" / "verify",
        Menu.i("UPLOAD_RESULTS") / "static" / "put",
        Menu.i("STATISTICS") / "static" / "statistics"),
      Menu.i("LESSONS") / "lessons" / "index" submenus (
        Menu.i("BASICS") / "lessons" / "basics" / "index" submenus (
          Menu.i("SETUP") / "lessons" / "basics" / "setup",
          Menu.i("HELLO_WORLD") / "lessons" / "basics" / "hello_world",
          Menu.i("USER_INTERFACES") / "lessons" / "basics" / "ui",
          Menu.i("PROJECT") / "lessons" / "basics" / "project"),
        Menu.i("WEBSERVICES") / "lessons" / "ws" / "index" submenus (
          Menu.i("CONSUME") / "lessons" / "ws" / "consume",
          Menu.i("PUT") / "lessons" / "ws" / "put",
          Menu.i("PROJECT_WS") / "lessons" / "ws" / "project"),
        Menu.i("SENSORS_SERVICES") / "lessons" / "sensors_and_services" / "index" submenus (
          Menu.i("STORAGE") / "lessons" / "sensors_and_services" / "storage",
          Menu.i("GEOLOCATION") / "lessons" / "sensors_and_services" / "geolocation",
          Menu.i("CAMERA") / "lessons" / "sensors_and_services" / "camera",
          Menu.i("PROJECT_SENSORS_AND_SERVICES")/ "lessons" / "sensors_and_services" / "project"),
        Menu.i("VISUALIZATION") / "lessons" / "visualization" / "index" submenus (
          Menu.i("TABLES") / "lessons" / "visualization" / "tables",
          Menu.i("DIAGRAMS") / "lessons" / "visualization" / "diagrams",
          Menu.i("PROJECT_STATISTICS") / "lessons" / "visualization" / "project")
        ))

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

    //    def localeCalculator(request: Box[HTTPRequest]): java.util.Locale = {
    //      def localeFromString(in: String): Locale = {
    //        val x = in.split("_").toList; new Locale(x.head, x.last)
    //      }
    //
    //      S.param("locale") match {
    //        case Full(null) => Locale.getDefault()
    //        case f @ Full(selectedLocale) => tryo(localeFromString(selectedLocale)) openOr(Locale.getDefault())
    //        case _ => Locale.getDefault()
    //      }
    //    }
    def localeCalculator(request: Box[HTTPRequest]) = new Locale("de", "DE")

    LiftRules.localeCalculator = localeCalculator _

    LiftRules.resourceNames = "i18n/messages" :: LiftRules.resourceNames

    LiftRules.dispatch.append(QuestionnaireServices)

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
}
