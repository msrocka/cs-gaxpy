# cs-gaxpy

Calling out for a sparse matrix vector multiplication into Rust function via
JNI. Because there is more JNI overhead than performance gain, it is not faster
than the pure Java function that is used in openLCA currently:

```
iteration | Java [ms] | JNI+Rust [ms]
0         | 0.775     | 3.220
1         | 0.671     | 3.138
2         | 0.668     | 3.142
3         | 0.635     | 3.143
4         | 0.632     | 3.045
5         | 0.652     | 2.993
6         | 0.662     | 3.078
7         | 0.665     | 3.413
8         | 0.642     | 3.130
9         | 0.666     | 3.133
```
