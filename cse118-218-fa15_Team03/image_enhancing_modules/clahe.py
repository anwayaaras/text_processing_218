import numpy as np
import cv2



img = cv2.imread('orig.jpg',0)
clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
cl1 = clahe.apply(img)
cv2.imwrite('clahe_result.jpg',cl1)