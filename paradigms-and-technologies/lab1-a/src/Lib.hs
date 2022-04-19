module Lib
    ( getWithOddFrequences
    ) where

-- count element occurances in a list
-- n - element to search for, x - first elem of the list xs
count :: Eq a => a -> [a] -> Int
count n [] = 0
count n (x:xs) | n == x = 1 + count n xs
           | otherwise = count n xs

-- count occurance frequency for all numbers
-- iterates over distinct elements composing a pair of number and number of occurances
-- Eq a => elements of the type a can be tested for equality
frequency :: Eq a => [a] -> [(a, Int)]
frequency [] = []
frequency (y:ys) = (y, count y (y:ys)) : frequency (filter (/= y) ys)

-- filter all numbers which happen in list for the odd number of times
filterOdd :: [(a, Int)] -> [(a, Int)]
filterOdd = filter (\(_,a) -> odd a)

-- compose a list from first elements of the tuples
takeFirst :: [(a, b)] -> [a]
takeFirst = map fst

-- compose function for more convenient use
getWithOddFrequences :: [Int] -> [Int]
getWithOddFrequences = takeFirst . filterOdd . frequency
