package bots.services

import infra.DBConnection
import play.api.Logger
import slick.jdbc.MySQLProfile.api._
import tables.Tables.{Histories, QuranIndonesia, Surah}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Creator: saifulbahri (bahrie172@gmail.com)
  * Date: 03/05/18
  * Time: 05.18
  */

object HikmahService {
    def toAyah(msg:String, userId:String): Future[String] = {
        Logger.info("get Quran by QS "+ msg)
        var surahNo = 1
        var ayahNo = 1
        val newMsg = msg.replace("/to ","")
        Logger.info("new msg "+ newMsg)
        if(newMsg.contains(":")){
            surahNo = newMsg.replace("Q","").replace("q", "").split(":")(0).toString.toInt
            ayahNo = newMsg.replace("Q","").replace("q", "").split(":")(1).toString.toInt
        }else{
            surahNo = newMsg.replace("Q","").replace("q", "").toString.toInt
            ayahNo = 1
        }

        Logger.info(s"Surah => $surahNo, ayah => $ayahNo")
        val action = for{
            history <-historiesTable.filter(_.user===userId).result.headOption
        }yield history

        DBConnection.db.run(action).flatMap{
            case Some(history) =>
                DBConnection.db.run(quranTable.filter(q => (q.suraid === surahNo && q.verseid === ayahNo)).result.headOption).map{
                    case Some(ayah) =>
                        updateChat(userId, ayah.id)
                        Logger.info(s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}")
                        s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}"
                    case None =>
                        Logger.info("not found")
                        "surat tidak ditemukan"

                }

            case None =>
                DBConnection.db.run(quranTable.filter(q => (q.suraid === surahNo && q.verseid === ayahNo)).result.headOption).map{
                    case Some(ayah) =>
                        storingChat(userId, ayah.id)
                        Logger.info(s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}")
                        s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}"
                    case None =>
                        Logger.info("not found")
                        "surah tidak ditemukan"
                }
        }
    }

    def nextReadingQuran(userId:String): Future[String] = {
        Logger.info(userId)
        val action = for{
            history <- historiesTable.filter(_.user===userId).result.headOption
        }yield history

        DBConnection.db.run(action).flatMap{
            case Some(history) => DBConnection.db.run(quranTable.filter(_.id===history.quranId+1).result.headOption).map{
                case Some(ayah) =>
                    updateChat(userId, history.quranId+1)
                    s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}"

                case None =>
                    "please to commend: /to Q1:1"
//                    DBConnection.db.run(quranTable.filter(_.id===1).result.headOption).map{
//                    case Some(ayah) =>
//                        updateChat(userId, 1)
//                        s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}"
//                    case None => "not found"
//                }

            }

            case None => DBConnection.db.run(quranTable.filter(_.id===1).result.head).map{
                ayah =>
                    storingChat(userId, 1)
                    s"${ayah.ayaharab.get}, \nQ${ayah.suraid}:${ayah.verseid}. ${ayah.ayahtext}"

            }
        }
    }

    def storingChat(userId:String, idQuran: Int)= {
        DBConnection.db.run(historiesTable.map(mt => (mt.user, mt.quranId)) +=
            (userId, idQuran))
    }

    def updateChat(userId:String, idQuran: Int)= {
        DBConnection.db.run(historiesTable.filter(_.user===userId).map(_.quranId).update(idQuran))
    }

    val quranTable= TableQuery[QuranIndonesia]
    val historiesTable = TableQuery[Histories]
    val surahTable = TableQuery[Surah]
}
