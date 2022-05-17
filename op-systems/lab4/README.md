## Disabling cache using CR0 registry
https://wiki.osdev.org/CPU_Registers_x86#CR0
30	CD	Cache disable

## Results
- Cache enabled:
    - 100 x 100 x 100
      ```
      Time: 1570ms
      Time: 1605ms
      Time: 1526ms
      ```
- Cache disabled:
    - 100 x 100 x 100
      ```
      Time: 1507ms
      Time: 1493ms
      Time: 1517ms
      ```