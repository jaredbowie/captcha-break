(ns captchabreak.core
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            )
  )

;currently only gets the ugly version of the captcha....as getting javascript to work is a bit of ... work

(def some-http-request
  {:headers {
            "User-Agent" "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"
            "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
            "Accept-Language" "en-US,en;q=0.8"
            "Accept-Encoding" "gzip,deflate,sdch"
            "Cache-Control" "no-cache"
            "Connection" "keep-alive"
            ;"Content-Type" "application/x-www-form-urlencoded"
            ;"Content-Encoding" "gzip"
            }
   })

(defn- get-catpcha-image
  "get recaptcha image from site, returns image file"
  [site-url http-header]
  )

(defn test-get-captcha-image []
  (let [site-result (client/get "http://www.wmssamples.com/user_interaction/recaptcha.aspx" )
        body-site-result (site-result :body)
        ]
    (println body-site-result)
    (re-find #"http\://www\.google\.com/recaptcha/api/" body-site-result)
    )
  )

(defn- get-challenge [api-key http-header]
  (let [http-resp (client/get (str "http://www.google.com/recaptcha/api/challenge?k=" api-key) http-header)
        http-resp-body (http-resp :body)
        ]
    (second (re-find #"challenge : '([\w-_]+)\'" http-resp-body))
    )
  )

(defn- test-get-challenge []
  (get-challenge "6LcAmgAAAAAAADO6kxnkCF5LnukN_nKUJjxsS4UW" some-http-request)
  )

(defn- get-captcha-api-key [url http-header]
  (let [site-result (client/get url http-header)
        body-site-result (site-result :body)
        ]
    (second(re-find #"noscript\?k=(\w+)" body-site-result))
    )
  )

(defn- test-get-captcha-api-key []
  (get-captcha-api-key "http://www.wmssamples.com/user_interaction/recaptcha.aspx" some-http-request)
  )
