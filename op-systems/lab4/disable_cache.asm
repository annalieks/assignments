global main
main:
push rax
mov rax, cr0
or rax, 0x40000000
mov cr0, rax
wbinvd
pop rax