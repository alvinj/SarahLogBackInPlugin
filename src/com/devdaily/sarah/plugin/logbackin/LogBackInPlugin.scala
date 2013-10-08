package com.devdaily.sarah.plugin.logbackin

import com.devdaily.sarah.plugins._
import java.io._
import java.util.Properties
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.InputEvent

/**
 * Log the user back into the computer. The user's password
 * must be in our properties file (password=foo) format.
 */
class LogBackInPlugin extends SarahPlugin {

  val phrasesICanHandle = List("log back in", "log back into computer")
  val robot = new Robot
  
  var canonPluginDirectory = ""
  val relativePropertiesFileName = "LogBackIn.properties"
  val passwordKey = "password"
  var password = ""
  
  // sarah callback
  def textPhrasesICanHandle: List[String] = {
    return phrasesICanHandle
  }

  // sarah callback
  override def setPluginDirectory(dir: String) {
    canonPluginDirectory = dir
  }

  // sarah callback
  def startPlugin = {
    password = getPasswordFromConfigFile(getCanonPropertiesFilename)
  }

  // handle our phrase
  def handlePhrase(phrase: String): Boolean = {
    if (phrasesICanHandle.contains(phrase)) {
      doLogin(password)
      return true
    } else {
      return false
    }
  }

  def doLogin(password: String) {
    robot.setAutoDelay(40)
    robot.setAutoWaitForIdle(true)
    
    robot.delay(500)
    robot.mouseMove(40, 130)
    robot.delay(500)

    typeString(password)
    robot.delay(500)
    typeKeystroke(KeyEvent.VK_ENTER)
    
    robot.delay(1000)
  }
  
  def leftClick {
    robot.mousePress(InputEvent.BUTTON1_MASK)
    robot.delay(200)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
    robot.delay(200)
  }
  
  def typeKeystroke(i: Int) {
    robot.delay(40)
    robot.keyPress(i)
    robot.keyRelease(i)
  }

  def typeString(s: java.lang.String) {
    val bytes = s.getBytes
    for (b <- bytes) {
      var code = b.toInt
      // keycode only handles [A-Z] (which is ASCII decimal [65-90])
      if (code > 96 && code < 123) code = code - 32
      robot.delay(40)
      robot.keyPress(code)
      robot.keyRelease(code)
    }
  }
  
  def getCanonPropertiesFilename: String = {
    return canonPluginDirectory + PluginUtils.getFilepathSeparator + relativePropertiesFileName
  }
  
  // TODO handle exceptions
  def getPasswordFromConfigFile(canonConfigFilename: String): String = {
	val properties = new Properties()
	val in = new FileInputStream(canonConfigFilename)
	properties.load(in)
	in.close()
	return properties.getProperty(passwordKey);
  }

  
  
  
}




