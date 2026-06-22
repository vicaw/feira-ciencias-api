-- Permite cancelar convites sem violar CHECK constraint de status
ALTER TABLE convites_registro
DROP CONSTRAINT IF EXISTS convites_registro_status_check;

ALTER TABLE convites_registro
ADD CONSTRAINT convites_registro_status_check
CHECK (status IN ('PENDENTE', 'USADO', 'EXPIRADO', 'CANCELADO'));