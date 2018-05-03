package infra

/**
  * Created by saifulbahri on 2/17/17.
  */
import slick.jdbc.MySQLProfile.api._

object DBConnection {
    val db = Database.forConfig("db")
}