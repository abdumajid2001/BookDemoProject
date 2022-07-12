package listener

import connection.{ConnectionProperties, DBConnection}

import java.io.{File, FileInputStream}
import java.nio.file.Paths
import java.util.{Locale, Properties}
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
      var filesPath = property("filesPath").asInstanceOf[String]

      val connectionProperties: ConnectionProperties = new ConnectionProperties(
        property("url").asInstanceOf[String],
        property("username").asInstanceOf[String],
        property("password").asInstanceOf[String]
      )

      DBConnection.Init(connectionProperties);

      val sep = File.separator

      if (filesPath.isEmpty)
        filesPath = settingPath + sep + contextPath + sep + "files" + sep

      ensureFolderExists(filesPath)

      context.setAttribute("filesPath", filesPath)
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

    val property = new Properties()
    property.load(new FileInputStream(file))

    def k(key: String): String = {
      property.getProperty(key)
    }

    Map(
      "settingPath" -> file.getAbsolutePath,
      "filesPath" -> formatFolderPath(k("files_path")),
      "url" -> k("db.url"),
      "username" -> k("db.username"),
      "password" -> k("db.password")
    )

  }

  private def formatFolderPath(path: String): String =
    if (path.isEmpty || path.endsWith("/")) path
    else path + "/"


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
