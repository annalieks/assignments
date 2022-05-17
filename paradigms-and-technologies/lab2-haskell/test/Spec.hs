import Lib (splitList)
import Test.HUnit
import Prelude

testSplitList = TestCase $ do
  assertEqual "[1] -> [[1]]" [[1]] (splitList [1])
  assertEqual "[1, 2, 3] -> [[1], [2], [3]]" [[1], [2], [3]] (splitList [1, 2, 3])
  assertEqual "[2, 1, 3] -> [[2, 1], [3]]" [[2, 1], [3]] (splitList [2, 1, 3])
  assertEqual "[2, 1, 3, 2, 5, 3] -> [[2, 1], [3, 2], [5, 3]]" [[2, 1], [3, 2], [5, 3]] (splitList [2, 1, 3, 2, 5, 3])
  assertEqual "[2, 2, 2] -> [[2], [2], [2]]" [[2], [2], [2]] (splitList [2, 2, 2])
  assertEqual "[5, 3, 2, 3, 4] -> [[5, 3, 2], [3], [4]]" [[5, 3, 2], [3], [4]] (splitList [5, 3, 2, 3, 4])
  assertEqual "[2, 1, 2, 1, 2] -> [[2, 1], [2, 1], [2]]" [[2, 1], [2, 1], [2]] (splitList [2, 1, 2, 1, 2])
  assertEqual
    "[5, 4, 2, 8, 3, 1, 6, 9, 5] -> [[5, 4, 2], [8, 3, 1], [6], [9, 5]]"
    [[5, 4, 2], [8, 3, 1], [6], [9, 5]]
    (splitList [5, 4, 2, 8, 3, 1, 6, 9, 5])

tests = TestList [TestLabel "Test List is Split Correctly" testSplitList]

main = runTestTT tests
