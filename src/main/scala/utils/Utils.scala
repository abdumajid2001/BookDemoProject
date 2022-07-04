package utils

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.Reader

object Utils {
  val mapperVal: ObjectMapper = mapper()

  def mapper(): ObjectMapper = {

    mapper
  }


  def readValue[T](src: Reader, valueType: Class[T]): T = {
    mapperVal.readValue(src, valueType)
  }

}
