(ns snake-clj.matrix)

(defn arity-y [matrix]
  {:pre  [(vector? matrix)]
   :post [(integer? %)]}
  (count matrix))

(defn arity-x [matrix]
  {:pre  [(vector? matrix)]
   :post [(integer? %)]}
  (count (nth matrix 0)))

(defn get-at [matrix [x y]]
  {:pre [(vector? matrix)]}
  (-> matrix
      (nth y)
      (nth x)))

(defn set-at [matrix [x y] v]
  {:pre  [(vector? matrix)
          (vector? (first matrix))]
   :post [(vector? %)]}
  (let [line (nth matrix y)]
    (assoc matrix y (assoc line x v))))