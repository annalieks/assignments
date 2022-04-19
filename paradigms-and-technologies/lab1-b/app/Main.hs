module Main where

import Lib ( removeNSmallest )

main :: IO ()
main = do
  print (removeNSmallest [1, 2, 3] 1)
