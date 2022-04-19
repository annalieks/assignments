module Lib
  ( splitList,
  )
where

splitToSublist a [] = [[a]]
splitToSublist a xs@(y : ys)
  | a > head y = (a : y) : ys
  | otherwise = [a] : xs

splitList :: Ord a => [a] -> [[a]]
splitList = foldr splitToSublist []