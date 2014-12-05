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
  (is (= nil (right-of {:world nil :snake nil}))
      "When the world doesn't exist there is nothing right-of snake")
  (is (= :tail (right-of {:world [[nil]] :snake [[0 0]]}))
      "Right of a 1 cell world is always the tail of a snake")
  (is (= :apple (right-of {:world [[nil :apple]] :snake [[0 0]]}))
      "There is an apple right of snake"))
