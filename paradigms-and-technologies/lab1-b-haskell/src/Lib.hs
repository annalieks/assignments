module Lib
    ( removeNSmallest
    ) where

import Data.List (sort, delete)

removeAllFromList :: [Int] -> [Int] -> [Int]
removeAllFromList [] [] = []
removeAllFromList [] (_:_) = []
removeAllFromList (x:xs) lst
         | x `elem` lst = removeAllFromList xs (delete x lst)
         | otherwise = x : removeAllFromList xs lst

removeNSmallest :: [Int] -> Int -> [Int]
removeNSmallest x n
        | n >= length x = []
        | otherwise = removeAllFromList x (take n . sort $ x)