import Lib ( getWithOddFrequences )
import Prelude
import Test.HUnit

testListWithOddFrequences = TestCase $ do
  assertEqual "[] -> []" [] (getWithOddFrequences [])
  assertEqual "[1] -> [1]" [1] (getWithOddFrequences [1])
  assertEqual "[1, 1] -> []" [] (getWithOddFrequences [1, 1])
  assertEqual "[1, 1, 1] -> [1]" [1] (getWithOddFrequences [1, 1, 1])
  assertEqual "[1, 2, 3] -> [1, 2, 3]"  [1, 2, 3] (getWithOddFrequences [1, 2, 3])
  assertEqual "[1, 1, 2, 3] -> [1, 2, 3]" [2, 3] (getWithOddFrequences [1, 1, 2, 3]) 
  assertEqual "[1, 1, 2, 2] -> []" [] (getWithOddFrequences[1, 1, 2, 2])
  assertEqual "[1, 1, 1, 2, 2, 2] -> [1, 2]" [1, 2] (getWithOddFrequences [1, 1, 1, 2, 2, 2])

tests = TestList [TestLabel "Test List Of Elements With Odd Frequencies" (testListWithOddFrequences)]

main = runTestTT tests