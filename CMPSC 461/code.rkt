#lang racket/base

(define (merge lst1 lst2)
  (cond
    [(null? lst1) lst2]
    [(null? lst2) lst1]
    [else
      (if (< (car lst1) (car lst2))
        (cons (car lst1) (merge (cdr lst1) lst2))
        (cons (car lst2) (merge lst1 (cdr lst2)))
      )
    ]
  )
)
    
(define (findMax lst)
  (if (null? lst)
    0
    (let (
        [num1 (findMax (cdr lst))]
        [num2 (if (number? (car lst)) 
          (car lst) 
          (findMax (car lst))
        )])
      (if (< num1 num2)
        num2
        num1
      )
    )
  )
)

(define (depthOfList lst)
  (if (list? lst)
    (let ([lst (foldl (lambda (x acc) (append (if (list? x) (cons 0 x) '()) acc)) '() lst)])
      (if (null? lst)
        1
        (+ 1 (depthOfList lst))
      )
    )
    
    0
  )
)

(define (trunc a b x)
  (map 
    (lambda (elem) (cond [(< elem a) a] [(> elem b) b] [else elem])
  ) x)
)
