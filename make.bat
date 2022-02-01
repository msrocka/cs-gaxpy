@echo off

set target_dir=src\main\resources\org\openlca\cs\gaxpy

if not exist %target_dir% (
  mkdir "%target_dir%"
)

cargo build --release
copy target\release\csgaxpy.dll %target_dir%
