(ns gd.P-1-1-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

; DESCRIPTION: Color circle controled by mouse and keyboard

(def segment-count (atom 360))
(def radius 300)
(def width 800)
(def height 800)

(defn setup []
  (q/no-stroke)
  (q/color-mode :hsb 360 width height))


(defn vx [angle]
  (+ (/ width 2) (* (q/cos (q/radians angle)) radius)))

(defn vy [angle]
  (+ (/ height 2) (* (q/sin (q/radians angle)) radius)))

(defn draw-state [state]
  (let [angle-step (/ 360 @segment-count)
        angle-range (range 0 361 angle-step)]
    (q/background 360 0 height)
    (q/begin-shape :triangle-fan)
    (q/vertex (/ width 2) (/ height 2))
    (doseq [angle angle-range]
      (q/vertex (vx angle) (vy angle))
      (q/fill angle (q/mouse-x) (q/mouse-y)))
    (q/end-shape)))

(defn save-image []
  (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_1_2_01.jpg"))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :1 (reset! segment-count 360)
    :2 (reset! segment-count 45)
    :3 (reset! segment-count 24)
    :4 (reset! segment-count 12)
    :5 (reset! segment-count 6)
    :s (save-image)
    :S (save-image)))
(def sketch (atom nil))
(q/defsketch sketch
  :title "P_1_1_2_01"
  :size [width height]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)