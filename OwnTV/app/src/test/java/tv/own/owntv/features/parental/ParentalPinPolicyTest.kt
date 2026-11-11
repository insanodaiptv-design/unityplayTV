package tv.own.owntv.features.parental
import org.junit.Assert.*
import org.junit.Test
import tv.own.owntv.core.util.Pin
import tv.own.owntv.core.content.AdultCategoryClassifier
class ParentalPinPolicyTest {
 @Test fun defaultIsExactlyFourZeroes() {
  assertTrue(ParentalPinPolicy.verify("0000", null))
  listOf("", "0", "000", "00000", "1234").forEach { assertFalse(ParentalPinPolicy.verify(it,null)) }
 }
 @Test fun configuredHashReplacesDefaultWithoutResettingIt() {
  val hash=Pin.hash("4826")
  assertTrue(ParentalPinPolicy.verify("4826",hash))
  assertFalse(ParentalPinPolicy.verify("0000",hash))
  assertFalse(ParentalPinPolicy.verify("4825",hash))
 }
 @Test fun malformedHashNeverUnlocks() {
  assertFalse(ParentalPinPolicy.verify("0000","not-a-hash"))
 }
 @Test fun adultCategoryMarkersAndSafeTitlePhrases() {
  listOf("Filmes | Adultos", "Séries XXX", "+18", "18+").forEach { assertTrue(AdultCategoryClassifier.isAdult(it)) }
  assertFalse(AdultCategoryClassifier.isAdult("Adult Swim"))
  assertFalse(AdultCategoryClassifier.isAdult("Comédia"))
 }
 @Test fun grantNeverLeaksAcrossProfilesOrContentsAndIsConsumedOnlyOnce() {
  val grant=ContentGrant()
  grant.allow(1L to "movie:42")
  assertFalse(grant.consume(2L to "movie:42"))
  assertFalse(grant.consume(1L to "series:42"))
  assertTrue(grant.consume(1L to "movie:42"))
  assertFalse(grant.consume(1L to "movie:42"))
 }
 @Test fun closingTheEntryRevokesTheGrant() {
  val grant=ContentGrant()
  grant.allow(1L to "movie:42")
  grant.revoke()
  assertFalse(grant.matches(1L to "movie:42"))
 }
 @Test fun missingClassificationFailsClosedAndOrdinaryCategoriesStayUnrestricted() {
  assertTrue(ParentalPinPolicy.requiresPin(null))
  assertTrue(ParentalPinPolicy.requiresPin(""))
  assertTrue(ParentalPinPolicy.requiresPin("Filmes | Adultos"))
  assertFalse(ParentalPinPolicy.requiresPin("Filmes | Lançamentos"))
 }
}
