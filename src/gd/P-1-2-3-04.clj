
(ns generative-design.P-1-2-3-03
  [:require [quil.core :as q]
   [quil.middleware :as m]])

(def colorCount 20)
(def alphaValue 75)
(def DISPLAY-PROB 0.55)

(defn random-float
  ([max] (random-float 0 max))
  ([min max] (+ min (* (rand) (- max min)))))

(defn random-vector-len-n
  ([val n] (take n (repeatedly #(random-float val))))
  ([min max n] (take n (repeatedly #(random-float min max)))))
(defn constant-vector-len-n [val n] (vec (repeat n val)))
(defn interleave-2-vectors [v1 v2] (mapcat vector v1 v2))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke))

(defn ensure-vector [input]
  (cond
    (float? input) [input] ; If input is a float, wrap it in a vector
    :else input))

(defn get-parts [i]
  (let [partCount (+ i 1)
        mask (repeatedly partCount #(< (random-float 1) 0.075))
        fragments (repeatedly partCount #(int (random-float 2 20)))
        generator #(random-float 2)
        parts (map #(repeatedly % generator) fragments)
        else-branch (repeatedly partCount #(random-float 2))
        params (map vector mask parts else-branch)]
    (mapcat (fn [[mask-i parts-i else-branch-i]]
              (if mask-i  (ensure-vector parts-i)  (ensure-vector else-branch-i))) params)))


(defn gradient-v2 [x y w h c2]
  (q/begin-shape)
  (q/fill (q/color 0))
  (q/vertex x y)
  (q/vertex (+ x w) y)
  (q/fill c2)
  (q/vertex (+ x w) (+ y h))
  (q/vertex x (+ y h))
  (q/end-shape :close))

(defn random-gradient [x y w h c2]
  (when (< (random-float 1) DISPLAY-PROB)
    (gradient-v2 x y w h c2)))

(defn draw-state [state]
  (let
   [noLoop (q/no-loop)
    background (q/background 0)
    randomSeed (q/random-seed (random-float 100000))
    hueValues (interleave-2-vectors (random-vector-len-n 360 colorCount) (constant-vector-len-n 195 colorCount))
    saturationValues (interleave-2-vectors (constant-vector-len-n 100 colorCount) (random-vector-len-n 20 colorCount))
    brightnessValues (interleave-2-vectors (random-vector-len-n 100 colorCount) (constant-vector-len-n 100 colorCount))
    rowCount (int (random-float 5 30))
    rowHeight (/ (q/height) rowCount)
    index (range rowCount -1 -1)
    parts_ (map get-parts index)
    counts (reductions + (map #(count %) parts_))
    params (map vector index parts_ counts)
    draw-row (fn  [i parts end-count]
               (let [sumPartsTotal (reduce + parts)
                     sumPartsNow (reductions + parts)
                     N (count parts)
                     counter (range (- end-count (count parts)) end-count)
                     index (map #(mod % colorCount) counter)
                     x (map #(q/map-range % 0 sumPartsTotal 0 (q/width)) sumPartsNow)
                     y (repeatedly N #(* i rowHeight))
                     w (map #(- (q/map-range % 0 sumPartsTotal 0 (q/width))) parts)
                     h (repeatedly N #(* 1.5 rowHeight))
                     col2 (map #(q/color (nth hueValues %) (nth saturationValues %) (nth brightnessValues %) alphaValue) index)
                     row-params (map vector x y w h col2)]
                 (doseq [[x y w h col] row-params] (random-gradient x y w h col))))]
    (doseq [[i parts end-count] params] (draw-row i parts end-count))))


(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_04.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_04.jpg")))


(defn mouse-released [_ __]
  (q/start-loop))

(q/defsketch sketch
  :title "test"
  :size [800 800]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed
  :mouse-released mouse-released
  :renderer :p3d)


