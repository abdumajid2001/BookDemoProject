package listener

import connection.{ConnectionProperties, DBConnection}
import setting.Setting

import java.io.{File, FileInputStream, FileOutputStream, Reader}
import java.nio.file.Paths
import java.util.{Locale, Properties}
import javax.servlet.annotation.WebListener
import javax.servlet.{ServletContext, ServletContextEvent, ServletContextListener}
import scala.util.control.ControlThrowable

class MyContextListener extends ServletContextListener {

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    Locale.setDefault(Locale.US)

    var context = sce.getServletContext

    try {
      val contextPath = context.getContextPath
      var settingPath = getSettingPath(context, contextPath)

      val property = loadProperties(settingPath, contextPath)

      var setting: Setting = Setting(
        contextPath = "",
        settingPath = property("settingPath").asInstanceOf[String],
        authKey = context.getInitParameter("auth_key"),
        maxUploadSize = property("maxUploadSize").asInstanceOf[Option[Long]],
        filesPath = property("filesPath").asInstanceOf[String]
      )

      val connectionProperties: ConnectionProperties = new ConnectionProperties(
        property("url").asInstanceOf[String],
        property("username").asInstanceOf[String],
        property("password").asInstanceOf[String],
        property("inactiveConnectionTimeout").asInstanceOf[String],
        property("maxConnectionReuse").asInstanceOf[String],
        property("initialPoolSize").asInstanceOf[String],
        property("minPoolSize").asInstanceOf[String],
        property("maxPoolSize").asInstanceOf[String]
      )

      DBConnection.Init(connectionProperties);

      val sep = File.separator

      if (setting.filesPath.isEmpty)
        setting = setting.copy(filesPath = settingPath + sep + contextPath + sep + "files" + sep)

      ensureFolderExists(setting.filesPath)

      setting = setting.copy(contextPath = contextPath)

      printSetting(setting)

      context.setAttribute("setting", setting)
    }
    catch {
      case ex: ControlThrowable => throw ex
      case ex: Throwable =>
        context.setAttribute("setting_error", ex)
        throw ex
    }
  }


  def loadProperties(settingPath: String, contextPath: String): Map[String, Any] = {
    val file = new File(settingPath, contextPath + ".properties")
    if (file.exists()) loadProperties(file) else loadProperties(createSettingFile(file))
  }

  private def loadProperties(file: File): Map[String, Any] = {

    var property = new Properties()
    property.load(new FileInputStream(file))

    def k(key: String): String = {
      property.getProperty(key)
    }

    Map(
      "settingPath" -> file.getAbsolutePath,
      "maxUploadSize" -> Option(k("max_upload_size")).filter(_.trim.nonEmpty).map(_.toLong),
      "filesPath" -> formatFolderPath(k("files_path")),
      "url" -> k("db.url"),
      "username" -> k("db.username"),
      "password" -> k("db.password"),
      "inactiveConnectionTimeout" -> k("db.inactiveConnectionTimeout"),
      "maxConnectionReuse" -> k("db.maxConnectionReuse"),
      "initialPoolSize" -> k("db.initialPoolSize"),
      "minPoolSize" -> k("db.minPoolSize"),
      "maxPoolSize" -> k("db.maxPoolSize")
    )

  }

  private def formatFolderPath(path: String): String =
    if (path.isEmpty || path.endsWith("/")) path
    else path + "/"

  private def printSetting(s: Setting): Unit = {
    println("*" * 70)
    println(s"Context path:${s.contextPath}")
    println(s"Setting path:${s.settingPath}")
    println(s"Files path  :${s.filesPath}")
    println("*" * 70)

  }


  def getSettingPath(context: ServletContext, contextPath: String): String = {
    if ((contextPath eq null) || contextPath.isEmpty) {
      Paths.get(context.getRealPath("/")).getParent.toString
    }
    else {
      var s = Option(context.getInitParameter("setting_path")).filter(_.nonEmpty).getOrElse("~/book")
      s = if (s.startsWith("~")) System.getProperty("user.home") + s.substring(1) else s
      ensureFolderExists(s)
      s
    }
  }

  private def ensureFolderExists(path: String): Unit = {
    var folders = new File(path)
    if (!folders.exists()) {
      folders.mkdirs()
    }
  }

  def createSettingFile(file: File): File = {
    val in = getClass.getResourceAsStream("/setting.properties")
    val out = new java.io.FileOutputStream(file)
    pipe(in, out)
    file
  }


  def pipe(in: java.io.InputStream, out: java.io.OutputStream): Unit = {
    val buffer = new Array[Byte](8192)

    @annotation.tailrec def loop(): Unit = {
      val byteCount = in.read(buffer)
      if (byteCount > 0) {
        out.write(buffer, 0, byteCount)
        loop()
      }
    }

    loop()
    in.close()
    out.close()
  }


}
