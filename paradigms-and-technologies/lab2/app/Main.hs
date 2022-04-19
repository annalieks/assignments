module Main where

import Lib ( splitList )

main :: IO ()
main = do
  print (splitList [1, 2, 4, 3, 2, 5, 4, 2, 6, 7, 4, 3])
  print (splitList [1, 2, 4])
  print (splitList [2, 1, 3])
--   print ([11] < head [1, 6]) -- compare sublists