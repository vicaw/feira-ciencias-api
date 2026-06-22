-- Adiciona CHECK constraint na coluna ano_escolar para aceitar apenas os valores do enum AnoEscolar
ALTER TABLE alunos
DROP CONSTRAINT IF EXISTS alunos_ano_escolar_check;

ALTER TABLE alunos
ADD CONSTRAINT alunos_ano_escolar_check
CHECK (ano_escolar IN (
    'EF_1','EF_2','EF_3','EF_4','EF_5','EF_6','EF_7','EF_8','EF_9',
    'EM_1','EM_2','EM_3'
));
