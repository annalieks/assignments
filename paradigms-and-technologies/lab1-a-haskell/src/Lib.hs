module Lib
    ( getWithOddFrequences
    ) where

count :: Eq a => a -> [a] -> Int
count n [] = 0
count n (x:xs) | n == x = 1 + count n xs
           | otherwise = count n xs

frequency :: Eq a => [a] -> [(a, Int)]
frequency [] = []
frequency (y:ys) = (y, count y (y:ys)) : frequency (filter (/= y) ys)

filterOdd :: [(a, Int)] -> [(a, Int)]
filterOdd = filter (\(_,a) -> odd a)

takeFirst :: [(a, b)] -> [a]
takeFirst = map fst

getWithOddFrequences :: [Int] -> [Int]
getWithOddFrequences = takeFirst . filterOdd . frequency
