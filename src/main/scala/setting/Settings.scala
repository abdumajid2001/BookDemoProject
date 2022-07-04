package setting

case class Setting(contextPath: String,
                   settingPath: String,
                   authKey: String,
                   maxUploadSize: Option[Long],
                   filesPath: String)
