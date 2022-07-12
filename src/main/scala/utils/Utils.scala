package utils

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.gson.Gson
import responce.{AppErrorDto, DataDto}

import java.io.Reader
import java.sql.{ResultSet, Statement}
import javax.servlet.ServletContext
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

object Utils {
  val mapperVal: ObjectMapper = mapper()

  val json: Gson = new Gson()

  def mapper(): ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper
  }


  def readValue[T](src: Reader, valueType: Class[T]): T = {
    mapperVal.readValue(src, valueType)
  }

  def isValidAuthKey(req: HttpServletRequest, resp: HttpServletResponse, getServletContext: ServletContext): Boolean = {
    try {
      resp.setContentType("application/json")
      if (!req.getHeader("auth_key").equals(getServletContext.getInitParameter("auth_key"))) {
        var response: DataDto[AppErrorDto] = new DataDto[AppErrorDto](new AppErrorDto("invalid auth key", "", req.getRequestURL.toString))
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(400)
        return false;
      }
    } catch {
      case e: Exception => {
        var response: DataDto[AppErrorDto] = new DataDto[AppErrorDto](new AppErrorDto("not found auth key", "", req.getRequestURL.toString))
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(400)
        return false
      }
    }
    return true
  }


  def isExistBook(st: Statement, req: HttpServletRequest, resp: HttpServletResponse, id: Int): Boolean = {
    var resultSet: ResultSet = st.executeQuery("select count(*) from RND_TEST.BOOKS where IS_DELETED = 'N' and ID = " + id)
    resultSet.next()
    if (resultSet.getInt(1) != 1) {
      var response = new DataDto[AppErrorDto](new AppErrorDto("Not found Book id = " + id, "", req.getRequestURL.toString));
      resp.getWriter.write(Utils.json.toJson(response))
      resp.setStatus(400)
      return false;
    }
    return true;
  }


}
