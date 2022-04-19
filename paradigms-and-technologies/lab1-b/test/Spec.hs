import Lib (removeNSmallest)
import Test.HUnit
import Prelude

testCorrectValues = TestCase $ do
  assertEqual "([1], 1) -> []" [] (removeNSmallest [1] 1)
  assertEqual "([4, 1, 1, 1, 5], 2) -> [4, 1, 5]" [4, 1, 5] (removeNSmallest [4, 1, 1, 1, 5] 2)
  assertEqual "([3, 5, 2, 1, 2, 2], 3) -> [3, 5, 2]" [3, 5, 2] (removeNSmallest [3, 5, 2, 1, 2, 2] 3)
  assertEqual "([5, 4, 4, 3, 2, 2, 1], 4) -> [5, 4, 4]" [5, 4, 4] (removeNSmallest [5, 4, 4, 3, 2, 2, 1] 4)
  assertEqual "([3, 3, 3], 3) -> []" [] (removeNSmallest [3, 3, 3] 3)

testNBiggerThanLength = TestCase $ do
  let expected = []
  assertEqual "([], 3) -> []" expected (removeNSmallest [] 3)
  assertEqual "([1], 10) -> []" expected (removeNSmallest [1] 10)

tests =
  TestList
    [ TestLabel "Test Correct Values" testCorrectValues,
      TestLabel "Test N Bigger Than List Length" testNBiggerThanLength
    ]

main = runTestTT tests