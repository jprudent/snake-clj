(ns snake-clj.db)

(def event-store (atom {}))

(defn load-aggregate [id])

(defn store! [& events]
  (doseq [{id :id :as event} events]
    (swap! event-store
           (fn [event-store]
             (update-in event-store [id] #(vec (conj % event)))))))

