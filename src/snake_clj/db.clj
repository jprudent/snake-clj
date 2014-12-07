(ns snake-clj.db
  (:require [snake-clj.events :as evt]
  ))

(def event-store (atom {}))

(defn load-aggregate [id]
  (reduce (fn [state event] (evt/handle-event state event)) nil (@event-store id)))

(defn store! [& events]
  (doseq [{id :id :as event} events]
    (swap! event-store
           (fn [event-store]
             (update-in event-store [id] #(vec (conj % event)))))))

