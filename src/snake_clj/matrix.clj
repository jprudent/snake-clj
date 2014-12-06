(ns snake-clj.matrix)

(defn arity-y [matrix]
  (count matrix))

(defn arity-x [matrix]
  (count (nth matrix 0)))

(defn get-at [matrix [x y]]
  (-> matrix
      (nth y)
      (nth x)))