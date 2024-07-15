(ns gd.P-2-0-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def width 550)
(def TAU (* 2 Math/PI))

(defn setup []
  (q/stroke-cap :square))

(defn draw-state [state]
  (q/background 255)
  (q/translate (/ width 2) (/ width 2))
  (q/stroke-weight (/ (q/mouse-y) 20))

  (let [circle-resolution (int (q/map-range (q/mouse-y) 0 width 2 80))
        radius (- (q/mouse-x) (/ width 2))
        angle (/ TAU circle-resolution)]
    (doseq [i (range circle-resolution)]
      (let [x (* radius (Math/cos (* i angle)))
            y (* radius (Math/sin (* i angle)))]
        (q/line 0 0 x y)))))

(defn key-pressed [state {:keys [key key-code]}]
  (when (or (= key :s) (= key :S))
    (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_2_0_01.jpg")))

(def sketch (atom nil))
(q/defsketch sketch
  :host "sketch-canvas"
  :title "P_2_0_01"
  :size [width width]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)