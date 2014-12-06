(ns snake-clj.core-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]))

#_(deftest test-right-of
  (let [aggregate {:world-dimension 2
                   :world           [nil :wall
                                     nil :apple]}]
    (testing "right-of when snake one cell long"
      (is (= [1 1 :wall] (right-of (assoc aggregate :snake [[0 1]])))))
    #_(testing "right-of when snake head is not on last column"
      (is (= [1 1 :wall] (right-of {:snake [[0 0] [0 1]]
                              :world world})))
      (is (= [0 0] (right-of {:snake [[1 1] [1 0]]
                                    :world world}))))))

(deftest test-right-of

  (is (= :tail (right-of (snake-state [[nil]] [[0 0]] :up)))
      "Right of a 1 cell world is always the tail of a snake")
  (is (= :apple (right-of (snake-state [[nil :apple]] [[0 0]] :up)))
      "There is an apple right of snake")
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil nil]]]
    (is (= :wall (right-of (snake-state world [[1 1] [1 0]] :up)))
        "Can turn right when going up")
    (is (= :apple (right-of (snake-state world [ [1 1] [1 2]] :down)))
        "Can turn right when going down")))
