(ns snake-clj.core-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]))

(deftest test-right-of
  (is (= :tail (right-of (snake-state [[nil]] [[0 0]] :up)))
      "Right of a 1 cell world is always the tail of a snake")
  (is (= :apple (right-of (snake-state [[nil :apple]] [[0 0]] :up)))
      "There is an apple right of snake")
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]]
    (is (= :wall (right-of (snake-state world [[1 1] [1 0]] :up)))
        "Can  see right when going up")
    (is (= :apple (right-of (snake-state world [[1 1] [1 2]] :down)))
        "Can see right when going down")
    (is (= :wall (right-of (snake-state world [[1 1] [2 1]] :right)))
        "Can see right when going right")
    (is (= :apple (right-of (snake-state world [[1 0] [0 0]] :left)))
        "Can see right when going left")))

(deftest test-left-of
  (is (= :tail (left-of (snake-state [[nil]] [[0 0]] :up)))
      "Left of a 1 cell world is always the tail of a snake")
  (is (= nil (left-of (snake-state [[nil :apple]] [[1 0]] :up)))
      "There is nothing left of snake")
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]]
    (is (= nil (left-of (snake-state world [[1 1] [1 0]] :up)))
        "Can see left when going up")
    (is (= :wall (left-of (snake-state world [[1 1] [1 2]] :down)))
        "Can see left when going down")
    (is (= :wall (left-of (snake-state world [[1 1] [2 1]] :right)))
        "Can see left when going right")
    (is (= nil (left-of (snake-state world [[1 0] [0 0]] :left)))
        "Can see left when going left")))
