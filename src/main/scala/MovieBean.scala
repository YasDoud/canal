class MovieBean(
  val tconst: String,
  val titleType: String,
  val primaryTitle: String,
  val originalTitle: String,
  val isAdult: Boolean,
  val startYear: String,
  val endYear: String,
  val runtimeMinutes: String,
  val genres: String)
{
  override def toString: String = s"tconst: $tconst, titleType: $titleType, primaryTitle: $primaryTitle, originalTitle: $originalTitle, isAdult: $isAdult, startYear: $startYear, endYear: $endYear, runtimeMinutes: $runtimeMinutes, genres: $genres"
}

object MovieBean {
  def apply(strReprentation: String): MovieBean = {

    val splited = strReprentation.split("\t")
    if (splited.length != 9) throw new IllegalArgumentException("str not valid !")
    return new MovieBean(splited(0),
                         splited(1),
                         splited(2),
                         splited(3),
                         splited(4).equals("1"), // isAdult
                         splited(5),// startYear
                         splited(6),// endYear
                         splited(7),// runtimeMinutes
                         splited(8))// genres
  }

}