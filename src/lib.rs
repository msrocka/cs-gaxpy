use jni_sys::{jclass, jdoubleArray, jint, jintArray, jobject, JNIEnv};
use std::ptr;

extern crate jni_sys;
extern crate libc;

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_org_openlca_cs_gaxpy_CsGaxpy_mul(
  env: *mut JNIEnv,
  _class: jclass,
  n: jint,
  column_pointers: jintArray,
  row_indices: jintArray,
  matrix_values: jdoubleArray,
  x_values: jdoubleArray,
  y_values: jdoubleArray,
) {
  unsafe {
    // column pointers of A
    let (cols_A_ptr, cols_A) = get_array_i32(env, column_pointers);

    // row indices of A
    let (rows_A_ptr, rows_a) = get_array_i32(env, row_indices);

    // values of A
    let (values_A_ptr, values_A) = get_array_f64(env, matrix_values);

    // vectors x and y
    let (x_ptr, x) = get_array_f64(env, x_values);
    let (y_ptr, y_slice) = get_array_f64(env, y_values);
    let y = y_slice as *mut [f64]; // y must be mutable

    for j in 0..(n as usize) {
      let p_start = (*cols_A)[j] as usize;
      let p_end = (*cols_A)[j + 1] as usize;
      let xj = (*x)[j];
      for p in p_start..p_end {
        let i = (*rows_a)[p] as usize;
        let aij = (*values_A)[p];
        (*y)[i] += aij * xj;
      }
    }

    // release arrays
    release_array_i32(env, column_pointers, cols_A_ptr);
    release_array_i32(env, row_indices, rows_A_ptr);
    release_array_f64(env, matrix_values, values_A_ptr);
    release_array_f64(env, x_values, x_ptr);
    release_array_f64(env, y_values, y_ptr);
  }
}

unsafe fn get_array_i32(
  env: *mut JNIEnv,
  array: jintArray,
) -> (*mut i32, *const [i32]) {
  let len = get_array_len(env, array);
  let jni_fn = (**env).GetIntArrayElements.unwrap();
  let raw_ptr = jni_fn(env, array, ptr::null_mut());
  let slice_ptr = ptr::slice_from_raw_parts(raw_ptr, len);
  return (raw_ptr, slice_ptr);
}

unsafe fn get_array_f64(
  env: *mut JNIEnv,
  array: jdoubleArray,
) -> (*mut f64, *const [f64]) {
  let len = get_array_len(env, array);
  let jni_fn = (**env).GetDoubleArrayElements.unwrap();
  let raw_ptr = jni_fn(env, array, ptr::null_mut());
  let slice_ptr = ptr::slice_from_raw_parts(raw_ptr, len);
  return (raw_ptr, slice_ptr);
}

unsafe fn get_array_len(env: *mut JNIEnv, array: jobject) -> usize {
  let jni_fn = (**env).GetArrayLength.unwrap();
  return jni_fn(env, array) as usize;
}

unsafe fn release_array_f64(
  env: *mut JNIEnv,
  array: jdoubleArray,
  raw_ptr: *mut f64,
) {
  let jni_fn = (**env).ReleaseDoubleArrayElements.unwrap();
  jni_fn(env, array, raw_ptr, 0);
}

unsafe fn release_array_i32(
  env: *mut JNIEnv,
  array: jintArray,
  raw_ptr: *mut i32,
) {
  let jni_fn = (**env).ReleaseIntArrayElements.unwrap();
  jni_fn(env, array, raw_ptr, 0);
}
