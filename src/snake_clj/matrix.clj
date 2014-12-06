(ns snake-clj.matrix)

(defn arity-y [matrix]
  (count matrix))

(defn arity-x [matrix]
  (count (nth matrix 0)))

(defn get-at [matrix [x y]]
  (-> matrix
      (nth y)
      (nth x)))

(defn rand-position [matrix]
  (let [x (rand (arity-x matrix))
        y (rand (arity-y matrix))]
    [x y]))