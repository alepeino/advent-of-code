(ns day13.core_spec
  (:require [clojure.test :refer :all])
  (:require [speclj.core :refer :all])
  (:require [day13.a :refer :all]))

(describe "When both values are integers"
  (it "If the left integer is lower than the right integer, the inputs are in the right order"
    (should (before? 1 2)))

  (it "If the left integer is higher than the right integer, the inputs are not in the right order"
    (should-not (before? 2 1))))

(describe "When both values are lists"
  (it "Should compare first item of both"
    (should (before? [1 1] [2 0])))

  (it "Should evaluate second item of lists when first is undecided"
    (should (before? [1 1] [1 2])))

  (it "Shorter list is before longer if undecided"
    (should (before? [1] [1 1]))
    (should-not (before? [1 1] [1])))

  (it "Should exhaust nested list and decide on second item"
    (should (before? [[1] 1] [[1] 2])))

  (it "Recurring on empty list should decide on shorter list"
    (should (before? [] [[]]))))

(describe "When only one is an integer"
  (it "Should compare it as list"
    (should (before? 1 [2]))
    (should (before? [1] 2))))

(run-specs)
