package bots

import bots.services.HikmahService
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import play.api.Logger

object HikmahBot
  extends TelegramBot // the general bot behavior
    with Polling // we use Polling
    with Commands { // and we want to listen to Commands

  lazy val token: String = botToken // the token is required by the Bot behavior

  onCommand('hello) { implicit msg => // listen for the command hello and
    reply(s"Hello ${msg.from.map(_.firstName).getOrElse("")}!") // and reply with the personalized greeting
  }

    onCommand('to) { implicit msg => // listen for the command hello and
        println(msg.toString)
        HikmahService.toAyah(msg.text.getOrElse(""), msg.from.map(_.id).getOrElse(0).toString).map{
            result =>
                Logger.info("result "+ result)
                reply(result) // and reply with the personalized greeting
        }
    }

    onCommand('next) { implicit msg => // listen for the command hello and
        println(msg.toString)
        HikmahService.nextReadingQuran(msg.from.map(_.id).getOrElse(0).toString).map{
            result =>
                reply(result) // and reply with the personalized greeting
        }
    }
}


object HikmahApp extends App {
  HikmahBot.run()
}



