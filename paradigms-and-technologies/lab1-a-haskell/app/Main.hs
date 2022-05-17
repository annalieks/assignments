module Main where

import Lib (getWithOddFrequences)

-- demonstation of how the lib works
main :: IO ()
main = do
  print(getWithOddFrequences [])
  print(getWithOddFrequences [1])
  print(getWithOddFrequences [1, 1, 2])
  print(getWithOddFrequences [1, 2, 2, 3, 3, 3, 4, 4, 4, 4])