package model

object Alphabets {
  val alphabets: Map[Languages.Value, String] = Map(
    Languages.Danish -> "abcdefghijklmnopqrstuvwxyzæåø",
    Languages.Finnish -> "abcdefghijklmnopqrstuvwxyzäö",
    Languages.Italian -> "abcdefghilmnopqrstuvz",
    Languages.Dutch -> "abcdefghijklmnopqrstuvwxyz",
    Languages.English -> "abcdefghijklmnopqrstuvwxyz",
    Languages.Portuguese -> "abcdefghijklmnopqrstuvwxyz",
    Languages.Slovenian -> "abcčdefghijklmnoprsštuvzž",
    Languages.Spanish -> "abcdefghijklmnñopqrstuvwxyz"
  )

  // klinkers
  val vowels: Map[Languages.Value, String] = Map(
    Languages.Danish -> "aeiouæåø",
    Languages.Finnish -> "aeiouäö",
    Languages.Italian -> "aeiou",
    Languages.Dutch -> "aeiou",
    Languages.English -> "aeiou",
    Languages.Portuguese -> "aeiou",
    Languages.Slovenian -> "aeiou",
    Languages.Spanish -> "aeiou"
  )

  // medeklinkers
  val consonants: Map[Languages.Value, String] = Map(
    Languages.Danish -> "bcdfghjklmnpqrstvwxyz",
    Languages.Finnish -> "bcdfghjklmnpqrstvwxyz",
    Languages.Italian -> "bcdfghlmnpqrstvz",
    Languages.Dutch -> "bcdfghjklmnpqrstvwxyz",
    Languages.English -> "bcdfghjklmnpqrstvwxyz",
    Languages.Portuguese -> "bcdfghjklmnpqrstvwxyz",
    Languages.Slovenian -> "bcčdfghjklmnprsštvzž",
    Languages.Spanish -> "bcdfghjklmnñpqrstvwxyz"
  )
}
