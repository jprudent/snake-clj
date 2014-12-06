(ns snake-clj.core-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]))

(deftest test-right-of
  (is (= :tail (right-of (snake-state [[nil]] [[0 0]] :up 42)))
      "Right of a 1 cell world is always the tail of a snake")
  (is (= :apple (right-of (snake-state [[nil :apple]] [[0 0]] :up 42)))
      "There is an apple right of snake")
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]]
    (is (= :wall (right-of (snake-state world [[1 1] [1 0]] :up 42)))
        "Can  see right when going up")
    (is (= :apple (right-of (snake-state world [[1 1] [1 2]] :down 42)))
        "Can see right when going down")
    (is (= :wall (right-of (snake-state world [[1 1] [2 1]] :right 42)))
        "Can see right when going right")
    (is (= :apple (right-of (snake-state world [[1 0] [0 0]] :left 42)))
        "Can see right when going left")))

(deftest test-left-of
  (is (= :tail (left-of (snake-state [[nil]] [[0 0]] :up 42)))
      "Left of a 1 cell world is always the tail of a snake")
  (is (= nil (left-of (snake-state [[nil :apple]] [[1 0]] :up 42)))
      "There is nothing left of snake")
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]]
    (is (= nil (left-of (snake-state world [[1 1] [1 0]] :up 42)))
        "Can see left when going up")
    (is (= :wall (left-of (snake-state world [[1 1] [1 2]] :down 42)))
        "Can see left when going down")
    (is (= :wall (left-of (snake-state world [[1 1] [2 1]] :right 42)))
        "Can see left when going right")
    (is (= nil (left-of (snake-state world [[1 0] [0 0]] :left 42)))
        "Can see left when going left")))

(deftest test-ahead-of
  (is (= :tail (ahead-of (snake-state [[nil]] [[0 0]] :up 42)))
      "Ahead of a 1 cell world is always the tail of a snake")
  (is (= :tail (ahead-of (snake-state [[nil :apple]] [[1 0]] :up 42)))
      "There is an apple ahead of snake")
  (let [world [[nil nil :wall]
                 [nil nil nil]
                 [:apple nil :wall]]]
    (is (= nil (ahead-of (snake-state world [[1 1] [1 0]] :up 42)))
        "Can see ahead when going up")
    (is (= nil (ahead-of (snake-state world [[1 1] [1 2]] :down 42)))
        "Can see ahead when going down")
    (is (= nil (ahead-of (snake-state world [[1 1] [2 1]] :right 42)))
        "Can see ahead when going right")
    (is (= :wall (ahead-of (snake-state world [[1 0] [0 0]] :left 42)))
        "Can see ahead when going left")))