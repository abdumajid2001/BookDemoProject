import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import entity.Book
import utils.Utils

import java.io.StringWriter

object Main {
  def main(args: Array[String]): Unit = {
    val person2 = Utils.mapper().readValue("{\n  \"name\": \"matematika\",\n   \"authName\": \"matematika\",\n    \"description\": \"matematika\",\n  \"pageCount\": \"52\"\n}", classOf[Book])
    println(person2)
  }
}

object TRoot {
  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  var person = new Person(2, "abdumajid")
  val out = new StringWriter
  mapper.writeValue(out, person)
  println(out)
}

