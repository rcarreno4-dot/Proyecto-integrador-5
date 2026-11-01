import React from "react";

interface KPICardProps {
  title: string;
  value: string | number;
  subtitle: string;
  icon: string;
  variant: "blue" | "green" | "amber" | "purple";
  onClick?: () => void;
  ctaText?: string;
}

export const KPICard: React.FC<KPICardProps> = ({
  title,
  value,
  subtitle,
  icon,
  variant,
  onClick,
  ctaText = "Ver detalle"
}) => {
  const getColors = () => {
    switch (variant) {
      case "blue":
        return { bg: "#eff6ff", border: "#bfdbfe", text: "#1e40af", iconBg: "#dbeafe" };
      case "green":
        return { bg: "#f0fdf4", border: "#bbf7d0", text: "#166534", iconBg: "#dcfce7" };
      case "amber":
        return { bg: "#fffbeb", border: "#fde68a", text: "#92400e", iconBg: "#fef3c7" };
      case "purple":
        return { bg: "#faf5ff", border: "#e9d5ff", text: "#6b21a8", iconBg: "#f3e8ff" };
    }
  };

  const colors = getColors();

  return (
    <div
      onClick={onClick}
      className="card p-3 h-100 rounded-3 border transition-all"
      style={{
        background: colors.bg,
        borderColor: colors.border,
        cursor: onClick ? "pointer" : "default",
        boxShadow: "0 2px 4px rgba(0,0,0,0.02)"
      }}
    >
      <div className="d-flex align-items-center gap-3">
        <div
          className="rounded-3 d-flex align-items-center justify-content-center fs-3"
          style={{ width: 50, height: 50, background: colors.iconBg }}
        >
          {icon}
        </div>
        <div className="flex-grow-1">
          <div className="text-muted text-uppercase fw-bold" style={{ fontSize: "0.72rem" }}>
            {title}
          </div>
          <h2 className="mb-0 fw-bold" style={{ color: colors.text, fontSize: "1.75rem" }}>
            {value}
          </h2>
          <small className="text-secondary" style={{ fontSize: "0.8rem" }}>
            {subtitle}
          </small>
        </div>
      </div>
      {onClick && (
        <div className="mt-3 pt-2 border-top d-flex justify-content-between align-items-center small fw-bold" style={{ color: colors.text }}>
          <span>{ctaText}</span>
          <span>➔</span>
        </div>
      )}
    </div>
  );
};
