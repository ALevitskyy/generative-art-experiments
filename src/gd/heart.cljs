(ns gd.heart
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def period 7)

(defn heart-point [s t]
  (let [x (* s (* 16 (q/pow (Math/sin t) 3)))
        y (* s (- (* 13 (q/cos t))
                  (* 5 (q/cos (* 2 t)))
                  (* 2 (q/cos (* 3 t)))
                  (q/cos (* 4 t))))]
    [x (- y)]))

(defn contraction [[x y]]
  [(* x 0.99)
   (* y 0.99)])

(defn distention [[x y]]
  [(/ x 0.99)
   (/ y 0.99)])

(defn setup []
  (q/frame-rate 20)
  {:points (mapv (partial heart-point (/ (q/height) 40))
                 (range 0 10 0.1))
   :scaling-function contraction
   :counter period})

(defn draw [state]
  (q/background 0)
  (q/fill 255 0 0)
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (q/begin-shape)
    (doseq [p (:points state)]
      (apply q/curve-vertex p))
    (q/end-shape)))

(def opposite-function
  {contraction distention
   distention contraction})

(defn beat [state]
  (if (zero? (:counter state))
    (-> state
        (assoc :counter period)
        (update :scaling-function opposite-function))
    (-> state
        (update :counter dec)
        (update :points #(mapv (:scaling-function state) %)))))

(def sketch (atom nil))
(defn run-sketch [] ;DELETE
  (js/console.log "run-sketch2") ;DELETE 
  (q/defsketch sketch
    :host "sketch-canvas"
    :size [400 400]
    :setup setup
    :draw draw
    :update beat
    :middleware [m/fun-mode])) ;DELETE

