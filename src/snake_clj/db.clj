(ns snake-clj.db
  (:require [snake-clj.events :as evt]
  ))

(def event-store (atom {}))

(defn load-aggregate
  "load the aggregate of id. Extra events can be provided to make simulations."
  [id & extra-events]
  (reduce (fn [state event] (evt/handle-event state event))
          nil
          (into (@event-store id) extra-events)))

(defn store! [& events]
  (doseq [{id :id :as event} events]
    (swap! event-store
           (fn [event-store]
             (update-in event-store [id] #(vec (conj % event)))))))

(defn delete! [id]
  (swap! event-store dissoc id))

